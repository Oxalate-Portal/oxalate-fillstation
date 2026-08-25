import {render, screen} from '@testing-library/react';
import {MemoryRouter} from 'react-router-dom';
import PrivateRoute from '../PrivateRoute';

const mockUseAuth = jest.fn();
jest.mock('../../context/AuthContext', () => ({useAuth: () => mockUseAuth()}));

const renderRoute = (requiredRole?: 'ROLE_ADMIN' | 'ROLE_OPERATOR' | 'ROLE_USER') =>
        render(
                <MemoryRouter initialEntries={['/protected']}>
                    <PrivateRoute requiredRole={requiredRole}>
                        <div>Protected content</div>
                    </PrivateRoute>
                </MemoryRouter>,
        );

describe('PrivateRoute', () => {
    test('loading_rendersSpinnerWithoutProtectedContent', () => {
        mockUseAuth.mockReturnValue({isLoading: true, isAuthenticated: false, user: null});
        renderRoute();
        expect(screen.queryByText('Protected content')).not.toBeInTheDocument();
        expect(document.querySelector('.ant-spin')).toBeInTheDocument();
    });

    test('anonymous_userIsRedirectedToLogin', () => {
        mockUseAuth.mockReturnValue({isLoading: false, isAuthenticated: false, user: null});
        renderRoute();
        expect(screen.queryByText('Protected content')).not.toBeInTheDocument();
    });

    test('wrongRole_userIsRedirectedWithoutProtectedContent', () => {
        mockUseAuth.mockReturnValue({isLoading: false, isAuthenticated: true, user: {roles: ['ROLE_USER']}});
        renderRoute('ROLE_ADMIN');
        expect(screen.queryByText('Protected content')).not.toBeInTheDocument();
    });

    test('authorized_userSeesProtectedContent', () => {
        mockUseAuth.mockReturnValue({isLoading: false, isAuthenticated: true, user: {roles: ['ROLE_ADMIN']}});
        renderRoute('ROLE_ADMIN');
        expect(screen.getByText('Protected content')).toBeInTheDocument();
    });
});
