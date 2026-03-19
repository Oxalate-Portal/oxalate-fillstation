import api from './axiosConfig';
import type { FillEntry } from '../types';

export const getFills = () => api.get('/api/fills');

export const getFill = (id: number) => api.get(`/api/fills/${id}`);

export const createFill = (data: Partial<FillEntry>) => api.post('/api/fills', data);

export const updateFill = (id: number, data: Partial<FillEntry>) =>
  api.put(`/api/fills/${id}`, data);

export const deleteFill = (id: number) => api.delete(`/api/fills/${id}`);
