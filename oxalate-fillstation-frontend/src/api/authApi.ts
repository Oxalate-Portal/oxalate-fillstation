import api from './axiosConfig';

export const login = (email: string, password: string) =>
  api.post('/api/auth/login', { email, password });

export const register = (data: { name: string; email: string; password: string; language: string }) =>
  api.post('/api/auth/register', data);

export const logout = () => api.post('/api/auth/logout');

export const verifyEmail = (token: string) =>
    api.get('/api/auth/verify-email', {params: {token}});

export const forgotPassword = (email: string) =>
  api.post('/api/auth/forgot-password', { email });

export const resetPassword = (token: string, newPassword: string) =>
  api.post('/api/auth/reset-password', { token, newPassword });
