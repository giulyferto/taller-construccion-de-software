import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './profesor.reducer';

export const ProfesorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const profesorEntity = useAppSelector(state => state.profesor.entity);
  const loading = useAppSelector(state => state.profesor.loading);
  const updating = useAppSelector(state => state.profesor.updating);
  const updateSuccess = useAppSelector(state => state.profesor.updateSuccess);

  const handleClose = () => {
    navigate(`/profesor${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...profesorEntity,
      ...values,
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
          ...profesorEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tallerConstruccionApp.profesor.home.createOrEditLabel" data-cy="ProfesorCreateUpdateHeading">
            <Translate contentKey="tallerConstruccionApp.profesor.home.createOrEditLabel">Create or edit a Profesor</Translate>
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
                  id="profesor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('tallerConstruccionApp.profesor.nombre')}
                id="profesor-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  pattern: {
                    // acepta letras (incluye acentos), espacios, guiones y apóstrofes; no acepta dígitos
                    value: /^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\s'-]+$/,
                    message: 'El nombre no puede contener números ni caracteres inválidos',
                  },
                  maxLength: { value: 100, message: 'Máximo 100 caracteres' },
                }}
              />
              <ValidatedField
                label={translate('tallerConstruccionApp.profesor.apellido')}
                id="profesor-apellido"
                name="apellido"
                data-cy="apellido"
                type="text"
                validate={{
                  pattern: {
                    value: /^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\s'-]+$/,
                    message: 'El apellido no puede contener números ni caracteres inválidos',
                  },
                  maxLength: { value: 100, message: 'Máximo 100 caracteres' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/profesor" replace color="info">
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

export default ProfesorUpdate;
