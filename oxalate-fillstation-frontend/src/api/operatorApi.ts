import api from './axiosConfig';
import type {UserStatus} from '../types';

export const getUsers = () => api.get('/api/operator/users');

export const getUser = (id: number) => api.get(`/api/operator/users/${id}`);

export const updateUserStatus = (id: number, status: UserStatus) =>
    api.post(`/api/operator/users/${id}/status`, {status});

export const getPendingRegistrations = () => api.get('/api/operator/registrations');

export const approveRegistration = (id: number) =>
  api.post(`/api/operator/registrations/${id}/approve`);

export const rejectRegistration = (id: number) =>
  api.post(`/api/operator/registrations/${id}/reject`);

export const zeroUserFills = (userId: number) =>
    api.post(`/api/operator/users/${userId}/fills/zero`);

export const notifyUsers = () => api.post('/api/operator/notify-users');
