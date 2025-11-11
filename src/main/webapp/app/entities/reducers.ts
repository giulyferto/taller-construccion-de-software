import alumno from 'app/entities/alumno/alumno.reducer';
import curso from 'app/entities/curso/curso.reducer';
import profesor from 'app/entities/profesor/profesor.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  alumno,
  curso,
  profesor,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
