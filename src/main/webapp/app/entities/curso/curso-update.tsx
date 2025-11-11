import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAlumnos } from 'app/entities/alumno/alumno.reducer';
import { getEntities as getProfesors } from 'app/entities/profesor/profesor.reducer';
import { createEntity, getEntity, reset, updateEntity } from './curso.reducer';

export const CursoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const alumnos = useAppSelector(state => state.alumno.entities);
  const profesors = useAppSelector(state => state.profesor.entities);
  const cursoEntity = useAppSelector(state => state.curso.entity);
  const loading = useAppSelector(state => state.curso.loading);
  const updating = useAppSelector(state => state.curso.updating);
  const updateSuccess = useAppSelector(state => state.curso.updateSuccess);

  const handleClose = () => {
    navigate(`/curso${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAlumnos({}));
    dispatch(getProfesors({}));
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

    const entity = {
      ...cursoEntity,
      ...values,
      alumnos: mapIdList(values.alumnos),
      profesor: profesors.find(it => it.id.toString() === values.profesor?.toString()),
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
          ...cursoEntity,
          alumnos: cursoEntity?.alumnos?.map(e => e.id.toString()),
          profesor: cursoEntity?.profesor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tallerConstruccionApp.curso.home.createOrEditLabel" data-cy="CursoCreateUpdateHeading">
            <Translate contentKey="tallerConstruccionApp.curso.home.createOrEditLabel">Create or edit a Curso</Translate>
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
                  id="curso-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('tallerConstruccionApp.curso.nombre')}
                id="curso-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                }}
              />
              <ValidatedField
                label={translate('tallerConstruccionApp.curso.descripcion')}
                id="curso-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <ValidatedField
                label={translate('tallerConstruccionApp.curso.alumno')}
                id="curso-alumno"
                data-cy="alumno"
                type="select"
                multiple
                name="alumnos"
              >
                <option value="" key="0" />
                {alumnos
                  ? alumnos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="curso-profesor"
                name="profesor"
                data-cy="profesor"
                label={translate('tallerConstruccionApp.curso.profesor')}
                type="select"
              >
                <option value="" key="0" />
                {profesors
                  ? profesors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/curso" replace color="info">
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

export default CursoUpdate;
