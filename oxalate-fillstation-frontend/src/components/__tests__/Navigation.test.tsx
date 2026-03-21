// Import from 'pure' to opt out of automatic cleanup (which uses synchronous act
// and cannot flush the Promise-based microtasks that antd Menu schedules).
import {act, render, screen} from '@testing-library/react/pure';
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
    it('renders build metadata in footer', async () => {
        await act(async () => {
            render(
                    <MemoryRouter>
                        <Navigation>
                            <div>page-content</div>
                        </Navigation>
                    </MemoryRouter>
            );
        });

        expect(screen.getByText(/nav.version:/)).toBeInTheDocument();
        expect(screen.getByText(/nav.buildDate:/)).toBeInTheDocument();
    });
});

