import api from '../axiosConfig';
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
});

