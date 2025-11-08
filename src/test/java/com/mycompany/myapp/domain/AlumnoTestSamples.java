package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlumnoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Alumno getAlumnoSample1() {
        return new Alumno().id(1L).dni(1).nombre("nombre1").apellido("apellido1");
    }

    public static Alumno getAlumnoSample2() {
        return new Alumno().id(2L).dni(2).nombre("nombre2").apellido("apellido2");
    }

    public static Alumno getAlumnoRandomSampleGenerator() {
        return new Alumno()
            .id(longCount.incrementAndGet())
            .dni(intCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString());
    }
}
