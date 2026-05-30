import api from './axiosConfig';
import type {Configuration} from '../types';

export const getConfig = () => api.get('/api/admin/config');

/** Fetches all users for administrator account management. */
export const getAdminUsers = () => api.get('/api/admin/users');

/** Activates the target user account. */
export const activateAdminUser = (id: number) => api.post(`/api/admin/users/${id}/activate`);

/** Sends a password reset email to the target user account. */
export const sendAdminPasswordReset = (id: number) => api.post(`/api/admin/users/${id}/password-reset`);

/** Closes the target user account without anonymizing personal data. */
export const closeAdminUser = (id: number) => api.post(`/api/admin/users/${id}/close`);

/** Anonymizes personal data for the target user account. */
export const anonymizeAdminUser = (id: number) => api.post(`/api/admin/users/${id}/anonymize`);

export const createConfig = (data: Omit<Configuration, 'id'>) =>
  api.post('/api/admin/config', data);

export const updateConfig = (id: number, data: Partial<Omit<Configuration, 'id'>>) =>
  api.put(`/api/admin/config/${id}`, data);

export const deleteConfig = (id: number) => api.delete(`/api/admin/config/${id}`);
