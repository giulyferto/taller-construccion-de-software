import { IAlumno } from 'app/shared/model/alumno.model';
import { IProfesor } from 'app/shared/model/profesor.model';

export interface ICurso {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  alumnos?: IAlumno[] | null;
  profesor?: IProfesor | null;
}

export const defaultValue: Readonly<ICurso> = {};
