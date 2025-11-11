package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.TipoAlumno;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Alumno} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlumnoDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer dni;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @NotNull
    private LocalDate fechaNacimiento;

    private TipoAlumno tipoAlumno;

    private BigDecimal notaPromedio;

    private Set<CursoDTO> cursos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public TipoAlumno getTipoAlumno() {
        return tipoAlumno;
    }

    public void setTipoAlumno(TipoAlumno tipoAlumno) {
        this.tipoAlumno = tipoAlumno;
    }

    public BigDecimal getNotaPromedio() {
        return notaPromedio;
    }

    public void setNotaPromedio(BigDecimal notaPromedio) {
        this.notaPromedio = notaPromedio;
    }

    public Set<CursoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(Set<CursoDTO> cursos) {
        this.cursos = cursos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlumnoDTO)) {
            return false;
        }

        AlumnoDTO alumnoDTO = (AlumnoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alumnoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlumnoDTO{" +
            "id=" + getId() +
            ", dni=" + getDni() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", tipoAlumno='" + getTipoAlumno() + "'" +
            ", notaPromedio=" + getNotaPromedio() +
            ", cursos=" + getCursos() +
            "}";
    }
}
