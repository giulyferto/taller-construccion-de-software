package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfesorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Profesor getProfesorSample1() {
        return new Profesor().id(1L).nombre("nombre1").apellido("apellido1");
    }

    public static Profesor getProfesorSample2() {
        return new Profesor().id(2L).nombre("nombre2").apellido("apellido2");
    }

    public static Profesor getProfesorRandomSampleGenerator() {
        return new Profesor().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString()).apellido(UUID.randomUUID().toString());
    }
}
