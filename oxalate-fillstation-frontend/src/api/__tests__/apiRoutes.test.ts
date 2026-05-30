import api from '../axiosConfig';
import {activateAdminUser, anonymizeAdminUser, closeAdminUser, getAdminUsers, sendAdminPasswordReset} from '../adminApi';
import {verifyEmail} from '../authApi';
import {getPendingRegistrations, notifyUsers, updateUserStatus, zeroUserFills,} from '../operatorApi';
import {anonymize} from '../userApi';

jest.mock('../axiosConfig', () => ({
    __esModule: true,
    default: {
        get: jest.fn(),
        post: jest.fn(),
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
});

