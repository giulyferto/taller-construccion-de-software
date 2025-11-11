package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TipoAlumno;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Alumno.
 */
@Entity
@Table(name = "alumno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alumno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "dni", nullable = false, unique = true)
    private Integer dni;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alumno")
    private TipoAlumno tipoAlumno;

    @Column(name = "nota_promedio", precision = 21, scale = 2)
    private BigDecimal notaPromedio;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "alumnos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alumnos", "profesor" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alumno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDni() {
        return this.dni;
    }

    public Alumno dni(Integer dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Alumno nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Alumno apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Alumno fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public TipoAlumno getTipoAlumno() {
        return this.tipoAlumno;
    }

    public Alumno tipoAlumno(TipoAlumno tipoAlumno) {
        this.setTipoAlumno(tipoAlumno);
        return this;
    }

    public void setTipoAlumno(TipoAlumno tipoAlumno) {
        this.tipoAlumno = tipoAlumno;
    }

    public BigDecimal getNotaPromedio() {
        return this.notaPromedio;
    }

    public Alumno notaPromedio(BigDecimal notaPromedio) {
        this.setNotaPromedio(notaPromedio);
        return this;
    }

    public void setNotaPromedio(BigDecimal notaPromedio) {
        this.notaPromedio = notaPromedio;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        if (this.cursos != null) {
            this.cursos.forEach(i -> i.removeAlumno(this));
        }
        if (cursos != null) {
            cursos.forEach(i -> i.addAlumno(this));
        }
        this.cursos = cursos;
    }

    public Alumno cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public Alumno addCurso(Curso curso) {
        this.cursos.add(curso);
        curso.getAlumnos().add(this);
        return this;
    }

    public Alumno removeCurso(Curso curso) {
        this.cursos.remove(curso);
        curso.getAlumnos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alumno)) {
            return false;
        }
        return getId() != null && getId().equals(((Alumno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alumno{" +
            "id=" + getId() +
            ", dni=" + getDni() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", tipoAlumno='" + getTipoAlumno() + "'" +
            ", notaPromedio=" + getNotaPromedio() +
            "}";
    }
}
