function startup()
    if file.open("init.lua") == nil then
        print("init.lua deleted or renamed")
    else
        print("Running")
        gpio.write(0, gpio.HIGH)
        tmr.delay(200000)
        file.close("init.lua")
        dofile("application.lua")
    end
end

gpio.mode(3, gpio.INT)
gpio.mode(4, gpio.INT)
--gpio.mode(3, gpio.INPUT)
--gpio.mode(4, gpio.INPUT)

gpio.mode(5, gpio.OUTPUT)
gpio.mode(6, gpio.OUTPUT)
gpio.mode(7, gpio.OUTPUT)
gpio.mode(8, gpio.OUTPUT)
gpio.mode(11, gpio.OUTPUT)

gpio.write(5, gpio.LOW)
gpio.write(6, gpio.LOW)
gpio.write(7, gpio.LOW)
gpio.write(8, gpio.LOW)
gpio.write(11, gpio.LOW)

print("Connecting to WiFi access point...")
gpio.write(0, gpio.LOW)

-- Wi-Fi AP 에 접속해 DHCP 에서 IP 를 할당 받은 뒤 메인 함수를 실행
wifi.setmode(wifi.STATION)
wifi.sta.config({ssid="TCD-IOT", pwd="phrtcd1215"})
wifi.sta.connect()

timer1 = tmr.create()
timer2 = tmr.create()
timer1:alarm(1000, tmr.ALARM_AUTO, function()
    if wifi.sta.getip() == nil then
        print("Waiting for IP address...")
    else
        timer1:stop()
        print("WiFi connection established, IP address: " .. wifi.sta.getip())
        print("You have 3 seconds to abort")
        print("Waiting...")
        timer2:alarm(3000, tmr.ALARM_SINGLE, startup)
    end
end)
