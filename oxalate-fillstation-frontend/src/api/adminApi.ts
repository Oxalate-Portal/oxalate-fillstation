import api from './axiosConfig';
import type { Configuration } from '../types';

export const getConfig = () => api.get('/api/admin/config');

export const createConfig = (data: Omit<Configuration, 'id'>) =>
  api.post('/api/admin/config', data);

export const updateConfig = (id: number, data: Partial<Omit<Configuration, 'id'>>) =>
  api.put(`/api/admin/config/${id}`, data);

export const deleteConfig = (id: number) => api.delete(`/api/admin/config/${id}`);
