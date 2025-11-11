import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './curso.reducer';

export const CursoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cursoEntity = useAppSelector(state => state.curso.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cursoDetailsHeading">
          <Translate contentKey="tallerConstruccionApp.curso.detail.title">Curso</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cursoEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="tallerConstruccionApp.curso.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{cursoEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="tallerConstruccionApp.curso.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{cursoEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="tallerConstruccionApp.curso.alumno">Alumno</Translate>
          </dt>
          <dd>
            {cursoEntity.alumnos
              ? cursoEntity.alumnos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nombre}</a>
                    {cursoEntity.alumnos && i === cursoEntity.alumnos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="tallerConstruccionApp.curso.profesor">Profesor</Translate>
          </dt>
          <dd>{cursoEntity.profesor ? cursoEntity.profesor.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/curso" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/curso/${cursoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CursoDetail;
