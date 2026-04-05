import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import type {AxiosResponse} from 'axios';
import React from 'react';
import FillsPage from '../FillsPage';
import type {Cylinder} from '../../types';
import {createFill, deleteFill, getFills, updateFill} from '../../api/fillApi';
import {getCylinders} from '../../api/cylinderApi';

jest.mock('react-i18next', () => ({
    useTranslation: () => ({t: (key: string) => key}),
}));

jest.mock('antd', () => {
    const actual = jest.requireActual('antd');
    return {
        ...actual,
        Table: () => <div data-testid="fills-table"/>,
        Modal: ({
                    open,
                    title,
                    children,
                    onOk,
                    onCancel,
                    okText,
                    cancelText,
                    okButtonProps,
                }: {
            open?: boolean;
            title?: React.ReactNode;
            children?: React.ReactNode;
            onOk?: () => void;
            onCancel?: () => void;
            okText?: React.ReactNode;
            cancelText?: React.ReactNode;
            okButtonProps?: { disabled?: boolean };
        }) => (open ? (
                <div>
                    <div>{title}</div>
                    {children}
                    <button type="button" onClick={onOk} disabled={okButtonProps?.disabled}>{okText}</button>
                    <button type="button" onClick={onCancel}>{cancelText}</button>
                </div>
        ) : null),
        Select: ({
                     options,
                     value,
                     onChange,
                     disabled,
                     placeholder,
                 }: {
            options?: Array<{ value: number; label: string }>;
            value?: number;
            onChange?: (value: number) => void;
            disabled?: boolean;
            placeholder?: string;
        }) => (
                <select
                        aria-label={placeholder}
                        role="combobox"
                        value={value ?? ''}
                        onChange={(event) => onChange?.(Number(event.target.value))}
                        disabled={disabled}
                >
                    <option value="">--</option>
                    {(options ?? []).map((option) => (
                            <option key={option.value} value={option.value}>{option.label}</option>
                    ))}
                </select>
        ),
    };
});

jest.mock('../../api/fillApi', () => ({
    getFills: jest.fn(),
    createFill: jest.fn(),
    updateFill: jest.fn(),
    deleteFill: jest.fn(),
}));

jest.mock('../../api/cylinderApi', () => ({
    getCylinders: jest.fn(),
}));

const mockGetFills = getFills as jest.MockedFunction<typeof getFills>;
const mockCreateFill = createFill as jest.MockedFunction<typeof createFill>;
const mockGetCylinders = getCylinders as jest.MockedFunction<typeof getCylinders>;

const mockResponse = <T, >(data: T): AxiosResponse<T> => ({data} as AxiosResponse<T>);

const cylinders: Cylinder[] = [
    {id: 1, userId: 42, name: 'Travel 1', volume: 12, workingPressure: 232, serialNumber: 'TR-001'},
    {id: 2, userId: 42, name: 'Travel 2', volume: 11, workingPressure: 232, serialNumber: 'TR-002'},
];

describe('FillsPageUTC', () => {
    beforeEach(() => {
        jest.clearAllMocks();
        mockGetFills.mockResolvedValue(mockResponse([]));
        mockCreateFill.mockResolvedValue(mockResponse({}));
        (updateFill as jest.MockedFunction<typeof updateFill>).mockResolvedValue(mockResponse({}));
        (deleteFill as jest.MockedFunction<typeof deleteFill>).mockResolvedValue(mockResponse({}));
    });

    it('fillForm_whenOpened_showsUserCylinderOptions_Ok', async () => {
        mockGetCylinders.mockResolvedValue(mockResponse(cylinders));

        render(<FillsPage/>);

        await waitFor(() => expect(mockGetCylinders).toHaveBeenCalled());

        fireEvent.click(screen.getByRole('button', {name: /fills.add/}));
        expect(await screen.findByText('Travel 1 (TR-001)')).toBeInTheDocument();
        expect(screen.getByText('Travel 2 (TR-002)')).toBeInTheDocument();
    });

    it('createFill_whenNoCylinders_disablesSubmitAndShowsHint_Ok', async () => {
        mockGetCylinders.mockResolvedValue(mockResponse([]));
        const user = userEvent.setup();

        render(<FillsPage/>);

        await waitFor(() => expect(mockGetCylinders).toHaveBeenCalled());
        await user.click(screen.getByRole('button', {name: /fills.add/}));

        expect(screen.getByText('fills.noCylindersHint')).toBeInTheDocument();
        expect(screen.getByRole('button', {name: 'common.save'})).toBeDisabled();
    });
});

