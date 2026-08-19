package com.aldahir.pacientes.entity;

import com.aldahir.commons.enums.EstadoRegistro;
import com.aldahir.commons.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "PACIENTES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = true, length = 50)
    private String apellidoMaterno;

    @Column(name = "EDAD", nullable = false)
    private Short edad;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "PESO", nullable = false)
    private Double peso;

    @Column(name = "ESTATURA", nullable = false)
    private Double estatura;

    @Column(name = "IMC", nullable = true)
    private Double imc;

    @Column(name = "NUM_EXPEDIENTE", nullable = false, length = 20)
    private String numExpediente;

    @Column(name = "TELEFONO", nullable = false, length = 10)
    private String telefono;

    @Column(name = "DIRECCION", nullable = false, length = 150)
    private String direccion;

    @Column(name = "ESTADO_REGISTRO")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoRegistro estadoRegistro;

    public void agregarNumExpediente(String numExpediente) {
        StringCustomUtils.validarTamanio(numExpediente, 20, 20, "El número de expediente debe tener 20 caracteres");
        this.numExpediente = numExpediente;
    }

    public void agregarEstadoRegistro(EstadoRegistro estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    private void validarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String telefono,
            String direccion
    ) {
        StringCustomUtils.validarTamanio(nombre, 5, 50, "El nombre debe tener entre 5 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno, 5, 50, "El apellido paterno debe tener entre 5 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno, 5, 50, "El apellido materno debe tener entre 5 y 50 caracteres");
        StringCustomUtils.validarTamanio(email, 5, 100, "El email debe tener entre 10 y 100 caracteres");
        StringCustomUtils.validarTamanio(telefono, 10, 10, "El teléfono debe tener 10 caracteres");
        StringCustomUtils.validarTamanio(direccion, 10, 150, "La dirección debe tener entre 10 y 150 caracteres");
    }

    public void actualizarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            Short edad,
            Double peso,
            Double estatura,
            String email,
            String telefono,
            String direccion
    ) {
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, telefono, direccion);
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public void calcularIMC() {
        this.imc = this.peso / (this.estatura * this.estatura);
    }

}
