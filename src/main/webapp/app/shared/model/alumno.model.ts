import dayjs from 'dayjs';
import { ICurso } from 'app/shared/model/curso.model';
import { TipoAlumno } from 'app/shared/model/enumerations/tipo-alumno.model';

export interface IAlumno {
  id?: number;
  dni?: number;
  nombre?: string;
  apellido?: string;
  fechaNacimiento?: dayjs.Dayjs;
  tipoAlumno?: keyof typeof TipoAlumno | null;
  notaPromedio?: number | null;
  cursos?: ICurso[] | null;
}

export const defaultValue: Readonly<IAlumno> = {};
