import { Role } from '../enums/Role';

export interface IUser {
  id: number;
  name: string;
  email: string;
  role: Role;
  token: string;
}
