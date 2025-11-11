import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCursos } from 'app/entities/curso/curso.reducer';
import { TipoAlumno } from 'app/shared/model/enumerations/tipo-alumno.model';
import { createEntity, getEntity, reset, updateEntity } from './alumno.reducer';

export const AlumnoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cursos = useAppSelector(state => state.curso.entities);
  const alumnoEntity = useAppSelector(state => state.alumno.entity);
  const loading = useAppSelector(state => state.alumno.loading);
  const updating = useAppSelector(state => state.alumno.updating);
  const updateSuccess = useAppSelector(state => state.alumno.updateSuccess);
  const tipoAlumnoValues = Object.keys(TipoAlumno);

  const handleClose = () => {
    navigate(`/alumno${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCursos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.dni !== undefined && typeof values.dni !== 'number') {
      values.dni = Number(values.dni);
    }
    if (values.notaPromedio !== undefined && typeof values.notaPromedio !== 'number') {
      values.notaPromedio = Number(values.notaPromedio);
    }

    const entity = {
      ...alumnoEntity,
      ...values,
      cursos: mapIdList(values.cursos),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          tipoAlumno: 'REGULAR',
          ...alumnoEntity,
          cursos: alumnoEntity?.cursos?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tallerConstruccionApp.alumno.home.createOrEditLabel" data-cy="AlumnoCreateUpdateHeading">
            <Translate contentKey="tallerConstruccionApp.alumno.home.createOrEditLabel">Create or edit a Alumno</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="alumno-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('tallerConstruccionApp.alumno.dni')}
                id="alumno-dni"
                name="dni"
                data-cy="dni"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('tallerConstruccionApp.alumno.nombre')}
                id="alumno-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('tallerConstruccionApp.alumno.apellido')}
                id="alumno-apellido"
                name="apellido"
                data-cy="apellido"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('tallerConstruccionApp.alumno.fechaNacimiento')}
                id="alumno-fechaNacimiento"
                name="fechaNacimiento"
                data-cy="fechaNacimiento"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('tallerConstruccionApp.alumno.tipoAlumno')}
                id="alumno-tipoAlumno"
                name="tipoAlumno"
                data-cy="tipoAlumno"
                type="select"
              >
                {tipoAlumnoValues.map(tipoAlumno => (
                  <option value={tipoAlumno} key={tipoAlumno}>
                    {translate(`tallerConstruccionApp.TipoAlumno.${tipoAlumno}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('tallerConstruccionApp.alumno.notaPromedio')}
                id="alumno-notaPromedio"
                name="notaPromedio"
                data-cy="notaPromedio"
                type="text"
              />
              <ValidatedField
                label={translate('tallerConstruccionApp.alumno.curso')}
                id="alumno-curso"
                data-cy="curso"
                type="select"
                multiple
                name="cursos"
              >
                <option value="" key="0" />
                {cursos
                  ? cursos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/alumno" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AlumnoUpdate;
