import {render, screen} from '@testing-library/react';
import GasUsageCard from '../GasUsageCard';
import type {GasUsageSummary} from '../../types';

jest.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));

const mockData: GasUsageSummary = {
  totalO2Added: 1500.5,
  totalHeAdded: 750.25,
  totalGasAdded: 5000.0,
  sinceLastZeroO2Added: 300.0,
  sinceLastZeroHeAdded: 150.0,
  sinceLastZeroGasAdded: 1000.0,
};

describe('GasUsageCard', () => {
  it('renders gas usage statistics', () => {
    render(<GasUsageCard data={mockData} />);
    expect(screen.getByText('dashboard.gasUsage')).toBeInTheDocument();
    expect(screen.getByText('dashboard.totalO2')).toBeInTheDocument();
    expect(screen.getByText('dashboard.totalHe')).toBeInTheDocument();
    expect(screen.getByText('dashboard.totalGas')).toBeInTheDocument();
  });
  it('renders since last zero statistics', () => {
    render(<GasUsageCard data={mockData} />);
    expect(screen.getByText('dashboard.sinceLastZeroO2')).toBeInTheDocument();
    expect(screen.getByText('dashboard.sinceLastZeroHe')).toBeInTheDocument();
    expect(screen.getByText('dashboard.sinceLastZeroGas')).toBeInTheDocument();
  });
});
