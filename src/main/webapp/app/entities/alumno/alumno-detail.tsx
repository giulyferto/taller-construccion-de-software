import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './alumno.reducer';

export const AlumnoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const alumnoEntity = useAppSelector(state => state.alumno.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="alumnoDetailsHeading">
          <Translate contentKey="tallerConstruccionApp.alumno.detail.title">Alumno</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{alumnoEntity.id}</dd>
          <dt>
            <span id="dni">
              <Translate contentKey="tallerConstruccionApp.alumno.dni">Dni</Translate>
            </span>
          </dt>
          <dd>{alumnoEntity.dni}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="tallerConstruccionApp.alumno.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{alumnoEntity.nombre}</dd>
          <dt>
            <span id="apellido">
              <Translate contentKey="tallerConstruccionApp.alumno.apellido">Apellido</Translate>
            </span>
          </dt>
          <dd>{alumnoEntity.apellido}</dd>
          <dt>
            <span id="fechaNacimiento">
              <Translate contentKey="tallerConstruccionApp.alumno.fechaNacimiento">Fecha Nacimiento</Translate>
            </span>
          </dt>
          <dd>
            {alumnoEntity.fechaNacimiento ? (
              <TextFormat value={alumnoEntity.fechaNacimiento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="tipoAlumno">
              <Translate contentKey="tallerConstruccionApp.alumno.tipoAlumno">Tipo Alumno</Translate>
            </span>
          </dt>
          <dd>{alumnoEntity.tipoAlumno}</dd>
          <dt>
            <span id="notaPromedio">
              <Translate contentKey="tallerConstruccionApp.alumno.notaPromedio">Nota Promedio</Translate>
            </span>
          </dt>
          <dd>{alumnoEntity.notaPromedio}</dd>
        </dl>
        <Button tag={Link} to="/alumno" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/alumno/${alumnoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlumnoDetail;
