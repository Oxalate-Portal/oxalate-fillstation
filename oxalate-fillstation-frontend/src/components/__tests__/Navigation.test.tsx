import {render, screen} from '@testing-library/react';
import {MemoryRouter} from 'react-router-dom';
import Navigation from '../Navigation';

jest.mock('../../context/AuthContext', () => ({
    useAuth: () => ({
        user: {
            userId: 1,
            name: 'Test User',
            email: 'test@example.com',
            roles: ['ROLE_USER'],
            language: 'en',
        },
        logout: jest.fn(async () => undefined),
    }),
}));

jest.mock('react-i18next', () => ({
    useTranslation: () => ({t: (key: string) => key}),
}));

describe('Navigation', () => {
    it('renders build metadata in footer', () => {
        render(
                <MemoryRouter>
                    <Navigation>
                        <div>page-content</div>
                    </Navigation>
                </MemoryRouter>
        );

        expect(screen.getByText(/nav.version:/)).toBeInTheDocument();
        expect(screen.getByText(/nav.buildDate:/)).toBeInTheDocument();
    });
});

