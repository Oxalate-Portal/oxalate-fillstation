import api from './axiosConfig';

export const getMe = () => api.get('/api/users/me');

export const updateMe = (data: { name?: string; email?: string; language?: string }) =>
  api.put('/api/users/me', data);

export const getLoginHistory = () => api.get('/api/users/me/login-history');

export const anonymize = () => api.delete('/api/users/me');

export const getGasUsage = (userId?: number) => {
  const url = userId ? `/api/users/${userId}/gas-usage` : '/api/users/me/gas-usage';
  return api.get(url);
};
