import {render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import type {AxiosResponse} from 'axios';
import OperatorPage from '../OperatorPage';
import {approveRegistration, getPendingRegistrations, getUsers, notifyUsers, rejectRegistration, updateUserStatus, zeroUserFills} from '../../api/operatorApi';
import {forgotPassword} from '../../api/authApi';

jest.mock('react-i18next', () => ({
    useTranslation: () => ({t: (key: string) => key}),
}));

jest.mock('../../api/operatorApi', () => ({
    approveRegistration: jest.fn(),
    getPendingRegistrations: jest.fn(),
    getUsers: jest.fn(),
    notifyUsers: jest.fn(),
    rejectRegistration: jest.fn(),
    updateUserStatus: jest.fn(),
    zeroUserFills: jest.fn(),
}));

jest.mock('../../api/authApi', () => ({
    forgotPassword: jest.fn(),
}));

const mockResponse = <T, >(data: T): AxiosResponse<T> => ({data} as AxiosResponse<T>);

const mockGetUsers = getUsers as jest.MockedFunction<typeof getUsers>;
const mockGetPendingRegistrations = getPendingRegistrations as jest.MockedFunction<typeof getPendingRegistrations>;
const mockUpdateUserStatus = updateUserStatus as jest.MockedFunction<typeof updateUserStatus>;
const mockForgotPassword = forgotPassword as jest.MockedFunction<typeof forgotPassword>;

describe('OperatorPageUTC', () => {
    beforeEach(() => {
        jest.clearAllMocks();
        mockGetUsers.mockResolvedValue(mockResponse([
            {
                id: 7,
                name: 'Active User',
                email: 'active@example.com',
                language: 'en',
                status: 'ACTIVE',
                roles: ['ROLE_USER'],
                createdAt: '2026-01-02T00:00:00',
            },
        ]));
        mockGetPendingRegistrations.mockResolvedValue(mockResponse([
            {
                id: 8,
                name: 'Pending User',
                email: 'pending@example.com',
                language: 'en',
                status: 'PENDING',
                roles: ['ROLE_USER'],
                createdAt: '2026-01-03T00:00:00',
            },
        ]));
        (approveRegistration as jest.MockedFunction<typeof approveRegistration>).mockResolvedValue(mockResponse({}));
        (rejectRegistration as jest.MockedFunction<typeof rejectRegistration>).mockResolvedValue(mockResponse({}));
        mockUpdateUserStatus.mockResolvedValue(mockResponse({}));
        mockForgotPassword.mockResolvedValue(mockResponse({}));
        (notifyUsers as jest.MockedFunction<typeof notifyUsers>).mockResolvedValue(mockResponse({}));
        (zeroUserFills as jest.MockedFunction<typeof zeroUserFills>).mockResolvedValue(mockResponse({}));
    });

    it('loads users on mount and allows operator actions_Ok', async () => {
        const user = userEvent.setup();

        render(<OperatorPage/>);

        await waitFor(() => {
            expect(mockGetUsers).toHaveBeenCalled();
            expect(mockGetPendingRegistrations).toHaveBeenCalled();
        });

        await user.click(screen.getByRole('button', {name: 'operator.sendPasswordReset'}));
        expect(mockForgotPassword).toHaveBeenCalledWith('active@example.com');

        await user.click(screen.getByRole('button', {name: 'operator.lock'}));
        expect(mockUpdateUserStatus).toHaveBeenCalledWith(7, 'LOCKED');
    });
});

