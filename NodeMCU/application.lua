isManual = false

srvSck = nil

personNum = 0

rstTimer = tmr.create()

isINWorking = false
isOUTWorking = false

function detIRin()
    if isOUTWorking == false then
        isINWorking = true
        
        local num = 0
        while gpio.read(4) ~= 1 do
            tmr.delay(1000)
            num = num + 1
    
            if num >= 500 then
                break
            end
        end
    
        if num < 500 then
            personNum = personNum + 1
            
            print("person in")
        
            ok, json = pcall(sjson.encode, {to = "phone", type = "person", num = personNum})
            print(json)
            if srvSck then
                srvSck:send(json.."\r\n\r\n")
            end
    
            while gpio.read(4) ~= 0 do
                tmr.delay(1000)
            end
        
            print("person ready")
            tmr.delay(700000)
        end
        isINWorking = false
    end
end

function detIRout()
    if isINWorking == false then
        isOUTWorking = true
        
        local num = 0
        while gpio.read(3) ~= 1 do
            tmr.delay(1000)
            num = num + 1
    
            if num >= 500 then
                break
            end
        end
    
        if num < 500 then
            if personNum > 0 then
                personNum = personNum - 1
            end
            
            print("person out")
        
            ok, json = pcall(sjson.encode, {to = "phone", type = "person", num = personNum})
            print(json)
            if srvSck then
                srvSck:send(json.."\r\n\r\n")
            end
    
            while gpio.read(3) ~= 0 do
                tmr.delay(1000)
                --print(gpio.read(3))
            end
        
            print("person ready")
            tmr.delay(700000)
        end
        isOUTWorking = false
    end
end

cdsMax = 1024 -- 실내가 가장 밝을 때의 측정 값
cdsMin = 9 -- 실내가 가장 어두울 때의 측정 값

-- cds(): CDS 측정값에 따라 전등의 수를 조절
function cds()
    cdsValue = adc.read(0)
    print(cdsValue)
    
    if isManual == false and personNum > 0 then
        if cdsValue > 1000 then
            gpio.write(5, gpio.LOW)
            gpio.write(6, gpio.LOW)
            gpio.write(7, gpio.LOW)
            gpio.write(8, gpio.LOW)
            gpio.write(11, gpio.LOW)
        elseif cdsValue > 800 then
            gpio.write(5, gpio.HIGH)
            gpio.write(6, gpio.LOW)
            gpio.write(7, gpio.LOW)
            gpio.write(8, gpio.LOW)
            gpio.write(11, gpio.LOW)
        elseif cdsValue > 600 then
            gpio.write(5, gpio.HIGH)
            gpio.write(6, gpio.HIGH)
            gpio.write(7, gpio.LOW)
            gpio.write(8, gpio.LOW)
            gpio.write(11, gpio.LOW)
        elseif cdsValue > 400 then
            gpio.write(5, gpio.HIGH)
            gpio.write(6, gpio.HIGH)
            gpio.write(7, gpio.HIGH)
            gpio.write(8, gpio.LOW)
            gpio.write(11, gpio.LOW)
        elseif cdsValue > 200 then
            gpio.write(5, gpio.HIGH)
            gpio.write(6, gpio.HIGH)
            gpio.write(7, gpio.HIGH)
            gpio.write(8, gpio.HIGH)
            gpio.write(11, gpio.LOW)
        else
            gpio.write(5, gpio.HIGH)
            gpio.write(6, gpio.HIGH)
            gpio.write(7, gpio.HIGH)
            gpio.write(8, gpio.HIGH)
            gpio.write(11, gpio.HIGH)
        end
    end

    if personNum == 0 and isManual == false then
        gpio.write(5, gpio.LOW)
        gpio.write(6, gpio.LOW)
        gpio.write(7, gpio.LOW)
        gpio.write(8, gpio.LOW)
        gpio.write(11, gpio.LOW)
    end
end

-- sendCds(): CDS 측정값을 읽어와 퍼센트로 변환 후 소켓을 통해 릴레이 서버로 전송
function sendCds()
    value = adc.read(0)
    percent = (math.floor(((value - cdsMin) / (cdsMax - cdsMin) * 100) * 100) / 100)
    ok, json = pcall(sjson.encode, {to = "phone", type = "cds", num = percent})
    print(json)
    if srvSck then
        srvSck:send(json.."\r\n")
    end
end

-- sendWatt(): 측정된 전압, 전류 값을 통해 전력 값을 계산 후 릴레이 서버로 전송
function sendWatt()
    busV = ina.getBusVoltage_V()
    shutV = ina.getShuntVoltage_mV()
    loadV = busV + (shutV / 1000)
    current = ina.getCurrent_mA()

    percent = math.floor(current / 1000 * loadV * 100) / 100

    if percent >= 13 then
        percent = 0
    end
    
    ok, json = pcall(sjson.encode, {to = "phone", type = "watt", num = percent})
    print(json)
    if srvSck then
        srvSck:send(json.."\r\n")
    end
end

ina = require("ina219")
ina.init()

gpio.trig(3, "high", detIRin)
gpio.trig(4, "high", detIRout)

tmr.create():alarm(600, tmr.ALARM_AUTO, cds)
tmr.create():alarm(4000, tmr.ALARM_AUTO, sendCds)
tmr.create():alarm(5000, tmr.ALARM_AUTO, sendWatt)

print("Connecting to Server...")

gpio.write(0, gpio.LOW)
tmr.delay(100000)

-- 서버 접속
srv = net.createConnection(net.TCP, 0)

srv:on("receive", function(sck, c)
    gpio.write(0, gpio.LOW)
    local t = sjson.decode(c)
    if t.to == "esp" then
        if t.type == "mode" then
            print(t.manual)
            isManual = t.manual
            if t.manual then
                gpio.write(5, gpio.LOW)
                gpio.write(6, gpio.LOW)
                gpio.write(7, gpio.LOW)
                gpio.write(8, gpio.LOW)
                gpio.write(11, gpio.LOW)
            end
        elseif t.type == "light" then
            mode = gpio.LOW
            if t.mode == 1 then
                mode = gpio.HIGH
            end

            if t.num == 1 then
                gpio.write(5, mode)
            elseif t.num == 2 then
                gpio.write(6, mode)
            elseif t.num == 3 then
                gpio.write(7, mode)
            elseif t.num == 4 then
                gpio.write(8, mode)
            elseif t.num == 5 then
                gpio.write(11, mode)
            end
                        
            print(t.num.." : "..t.mode)
        end
    end
    gpio.write(0, gpio.HIGH)
end)

srv:on("connection", function(sck, c)
    sck:send('{"volt" : '..ina.getBusVoltage_V()..', "amps" : '..ina.getCurrent_mA()..'}')
    srvSck = sck
end)

srv:connect(8107, "tyoj.me")
print("Connected")

gpio.write(0, gpio.HIGH)
