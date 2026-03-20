import React from 'react';
import { render, screen } from '@testing-library/react';
import LoginPage from '../LoginPage';

jest.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  useNavigate: () => mockNavigate,
  Link: ({ children, to }: { children: React.ReactNode; to: string }) => <a href={to}>{children}</a>,
}));

jest.mock('../../context/AuthContext', () => ({
  useAuth: () => ({ login: jest.fn(), isAuthenticated: false, isLoading: false, user: null }),
}));

describe('LoginPage', () => {
  it('renders login form', () => {
    render(<LoginPage />);
    expect(screen.getAllByText('auth.login').length).toBeGreaterThan(0);
  });
});

