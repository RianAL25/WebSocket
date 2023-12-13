package io.github.gabrielsizilio.websocket;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.Timeout;
import jakarta.ejb.TimerConfig;
import jakarta.ejb.TimerService;
import jakarta.websocket.EncodeException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Startup
@Singleton
public class HorarioService {

    @Resource
    TimerService tservice;
    private volatile List<String> horas;

    @PostConstruct
    public void init() {
        tservice.createIntervalTimer(1000, 1000, new TimerConfig());

        horas = new ArrayList<>();
    }

    @Timeout
    public void timeout() throws EncodeException {
        horas.clear();

        horas.add(pegaHora(ZoneId.of("America/Sao_Paulo")));
        horas.add(pegaHora(ZoneId.of("Asia/Tokyo")));
        horas.add(pegaHora(ZoneId.of("Australia/Sydney")));
        horas.add(pegaHora(ZoneId.of("Asia/Jakarta")));
        horas.add(pegaHora(ZoneId.of("Europe/Moscow")));

        Endpoint.send(horas);
    }

    public String pegaHora(ZoneId zone) {
        return LocalTime.now(zone).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

}
