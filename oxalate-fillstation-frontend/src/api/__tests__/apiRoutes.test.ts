import api from '../axiosConfig';
import {
    activateAdminUser,
    anonymizeAdminUser,
    closeAdminUser,
    createConfig,
    deleteConfig,
    getAdminUsers,
    getConfig,
    sendAdminPasswordReset,
    updateConfig
} from '../adminApi';
import {forgotPassword, login, logout, register, resetPassword, verifyEmail} from '../authApi';
import {getPendingRegistrations, notifyUsers, updateUserStatus, zeroUserFills,} from '../operatorApi';
import {anonymize, getGasUsage, getLoginHistory, getMe, updateMe} from '../userApi';
import {createCylinder, deleteCylinder, getCylinders, updateCylinder} from '../cylinderApi';
import {createFill, deleteFill, getFill, getFills, updateFill} from '../fillApi';

jest.mock('../axiosConfig', () => ({
    __esModule: true,
    default: {
        get: jest.fn(),
        post: jest.fn(),
        put: jest.fn(),
        delete: jest.fn(),
    },
}));

describe('api route mappings', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('verifyEmail_callsExpectedEndpointWithQueryParams_Ok', () => {
        verifyEmail('token-123');

        expect(api.get)
            .toHaveBeenCalledWith('/api/auth/verify-email', {params: {token: 'token-123'}});
    });

    test('operatorRoutes_callExpectedEndpoints_Ok', () => {
        updateUserStatus(10, 'ACTIVE');
        getPendingRegistrations();
        zeroUserFills(5);
        notifyUsers();

        expect(api.post)
            .toHaveBeenCalledWith('/api/operator/users/10/status', {status: 'ACTIVE'});
        expect(api.get)
            .toHaveBeenCalledWith('/api/operator/registrations');
        expect(api.post)
            .toHaveBeenCalledWith('/api/operator/users/5/fills/zero');
        expect(api.post)
            .toHaveBeenCalledWith('/api/operator/notify-users');
    });

    test('anonymize_callsExpectedEndpoint_Ok', () => {
        anonymize();

        expect(api.post)
            .toHaveBeenCalledWith('/api/users/me/anonymize');
    });

    test('adminUserRoutes_callExpectedEndpoints_Ok', () => {
        getAdminUsers();
        activateAdminUser(3);
        sendAdminPasswordReset(3);
        closeAdminUser(3);
        anonymizeAdminUser(3);

        expect(api.get)
            .toHaveBeenCalledWith('/api/admin/users');
        expect(api.post)
            .toHaveBeenCalledWith('/api/admin/users/3/activate');
        expect(api.post)
            .toHaveBeenCalledWith('/api/admin/users/3/password-reset');
        expect(api.post)
            .toHaveBeenCalledWith('/api/admin/users/3/close');
        expect(api.post)
            .toHaveBeenCalledWith('/api/admin/users/3/anonymize');
    });

    test('authRoutes_callExpectedEndpoints_Ok', () => {
        login('user@example.com', 'secret');
        register({name: 'User', email: 'user@example.com', password: 'secret', language: 'fi'});
        logout();
        forgotPassword('user@example.com');
        resetPassword('token', 'new-secret');

        expect(api.post)
            .toHaveBeenCalledWith('/api/auth/login', {email: 'user@example.com', password: 'secret'});
        expect(api.post)
            .toHaveBeenCalledWith('/api/auth/register', {
                name: 'User', email: 'user@example.com', password: 'secret', language: 'fi',
            });
        expect(api.post)
            .toHaveBeenCalledWith('/api/auth/logout');
        expect(api.post)
            .toHaveBeenCalledWith('/api/auth/forgot-password', {email: 'user@example.com'});
        expect(api.post)
            .toHaveBeenCalledWith('/api/auth/reset-password', {token: 'token', newPassword: 'new-secret'});
    });

    test('cylinderFillAndUserRoutes_callExpectedEndpoints_Ok', () => {
        const cylinder = {name: 'Tank', volume: 12, workingPressure: 200, serialNumber: 'ABC'};
        const fill = {cylinderId: 1, endPressure: 200};
        getCylinders();
        createCylinder(cylinder);
        updateCylinder(1, cylinder);
        deleteCylinder(1);
        getFills();
        getFill(2);
        createFill(fill);
        updateFill(2, fill);
        deleteFill(2);
        getConfig();
        createConfig({groupName: 'mail', configKey: 'from', configValue: 'a@b.test'});
        updateConfig(3, {configValue: 'b@b.test'});
        deleteConfig(3);
        getMe();
        updateMe({language: 'sv'});
        getLoginHistory();
        getGasUsage();
        getGasUsage(9);

        expect(api.get)
            .toHaveBeenCalledWith('/api/cylinders');
        expect(api.post)
            .toHaveBeenCalledWith('/api/cylinders', cylinder);
        expect(api.put)
            .toHaveBeenCalledWith('/api/cylinders/1', cylinder);
        expect(api.delete)
            .toHaveBeenCalledWith('/api/cylinders/1');
        expect(api.get)
            .toHaveBeenCalledWith('/api/fills');
        expect(api.get)
            .toHaveBeenCalledWith('/api/fills/2');
        expect(api.post)
            .toHaveBeenCalledWith('/api/fills', fill);
        expect(api.put)
            .toHaveBeenCalledWith('/api/fills/2', fill);
        expect(api.delete)
            .toHaveBeenCalledWith('/api/fills/2');
        expect(api.put)
            .toHaveBeenCalledWith('/api/users/me', {language: 'sv'});
        expect(api.get)
            .toHaveBeenCalledWith('/api/users/me/gas-usage');
        expect(api.get)
            .toHaveBeenCalledWith('/api/users/9/gas-usage');
    });
});
