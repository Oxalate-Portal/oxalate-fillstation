import {render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import type {AxiosResponse} from 'axios';
import AdminPage from '../AdminPage';
import {activateAdminUser, closeAdminUser, getAdminUsers, getConfig, sendAdminPasswordReset,} from '../../api/adminApi';

jest.mock('react-i18next', () => ({
    useTranslation: () => ({t: (key: string) => key}),
}));

jest.mock('../../api/adminApi', () => ({
    getConfig: jest.fn(),
    getAdminUsers: jest.fn(),
    activateAdminUser: jest.fn(),
    sendAdminPasswordReset: jest.fn(),
    closeAdminUser: jest.fn(),
    anonymizeAdminUser: jest.fn(),
    createConfig: jest.fn(),
    updateConfig: jest.fn(),
    deleteConfig: jest.fn(),
}));

const mockResponse = <T, >(data: T): AxiosResponse<T> => ({data} as AxiosResponse<T>);

const mockGetConfig = getConfig as jest.MockedFunction<typeof getConfig>;
const mockGetAdminUsers = getAdminUsers as jest.MockedFunction<typeof getAdminUsers>;
const mockActivateAdminUser = activateAdminUser as jest.MockedFunction<typeof activateAdminUser>;
const mockSendAdminPasswordReset = sendAdminPasswordReset as jest.MockedFunction<typeof sendAdminPasswordReset>;
const mockCloseAdminUser = closeAdminUser as jest.MockedFunction<typeof closeAdminUser>;

describe('AdminPageUTC', () => {
    beforeEach(() => {
        jest.clearAllMocks();
        mockGetConfig.mockResolvedValue(mockResponse([]));
        mockGetAdminUsers.mockResolvedValue(mockResponse([
            {
                id: 11,
                name: 'Pending User',
                email: 'pending@example.com',
                language: 'en',
                status: 'PENDING',
                roles: ['ROLE_USER'],
                createdAt: '2026-01-01T00:00:00',
            },
        ]));
        mockActivateAdminUser.mockResolvedValue(mockResponse({}));
        mockSendAdminPasswordReset.mockResolvedValue(mockResponse({}));
        mockCloseAdminUser.mockResolvedValue(mockResponse({}));
    });

    it('userActions_whenClicked_callAdminEndpoints_Ok', async () => {
        const user = userEvent.setup();

        render(<AdminPage/>);

        await waitFor(() => {
            expect(mockGetAdminUsers).toHaveBeenCalled();
        });

        await user.click(screen.getByRole('tab', {name: 'admin.users'}));

        await user.click(await screen.findByRole('button', {name: 'admin.activateUser'}));
        expect(mockActivateAdminUser).toHaveBeenCalledWith(11);

        await user.click(screen.getByRole('button', {name: 'admin.sendPasswordReset'}));
        expect(mockSendAdminPasswordReset).toHaveBeenCalledWith(11);

        await user.click(screen.getByRole('button', {name: 'admin.closeUser'}));
        await user.click(await screen.findByRole('button', {name: 'common.yes'}));
        expect(mockCloseAdminUser).toHaveBeenCalledWith(11);
    });
});

