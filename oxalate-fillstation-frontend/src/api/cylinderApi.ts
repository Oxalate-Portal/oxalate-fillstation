import api from './axiosConfig';
import type { Cylinder } from '../types';

export const getCylinders = () => api.get('/api/cylinders');

export const createCylinder = (data: Omit<Cylinder, 'id' | 'userId'>) =>
  api.post('/api/cylinders', data);

export const updateCylinder = (id: number, data: Partial<Omit<Cylinder, 'id' | 'userId'>>) =>
  api.put(`/api/cylinders/${id}`, data);

export const deleteCylinder = (id: number) => api.delete(`/api/cylinders/${id}`);
