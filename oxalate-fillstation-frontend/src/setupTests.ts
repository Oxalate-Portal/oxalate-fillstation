import '@testing-library/jest-dom';

declare const require: (moduleName: string) => {
    TextEncoder: typeof globalThis.TextEncoder;
    TextDecoder: typeof globalThis.TextDecoder;
};

if (!globalThis.TextEncoder || !globalThis.TextDecoder) {
    const util = require('util');
    if (!globalThis.TextEncoder) {
        globalThis.TextEncoder = util.TextEncoder;
    }
    if (!globalThis.TextDecoder) {
        globalThis.TextDecoder = util.TextDecoder;
    }
}

// Mock matchMedia for Ant Design components
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: jest.fn().mockImplementation((query: string) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: jest.fn(),
    removeListener: jest.fn(),
    addEventListener: jest.fn(),
    removeEventListener: jest.fn(),
    dispatchEvent: jest.fn(),
  })),
});
