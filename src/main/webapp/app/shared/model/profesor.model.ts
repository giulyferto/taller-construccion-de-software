export interface IProfesor {
  id?: number;
  nombre?: string;
  apellido?: string | null;
}

export const defaultValue: Readonly<IProfesor> = {};
