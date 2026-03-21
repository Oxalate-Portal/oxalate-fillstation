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

// Mock MessageChannel for Ant Design Form components (not available in jsdom).
// The callback must be deferred (via queueMicrotask) so that any pending
// array writes that precede the postMessage call complete before the handler
// fires – matching real MessageChannel semantics.
if (typeof MessageChannel === 'undefined') {
    class MockMessageChannel {
        port1: { onmessage: ((event: MessageEvent) => void) | null } = { onmessage: null };
        port2: { postMessage: (data: unknown) => void } = {
            postMessage: (data: unknown): void => {
                const handler = this.port1.onmessage;
                if (handler) {
                    queueMicrotask(() => handler(new MessageEvent('message', { data })));
                }
            },
        };
    }
    (globalThis as unknown as Record<string, unknown>).MessageChannel = MockMessageChannel;
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
