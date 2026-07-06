import '@testing-library/jest-dom/vitest';
import '@testing-library/jest-dom';
import { beforeAll, afterEach, afterAll } from 'vitest';
import { server } from './mocks/server';

// 1. SOLUCIÓN CORREGIDA PARA JAVASCRIPT: Quitamos el "as any" que rompía el compilador
import { ReadableStream } from 'node:stream/web';

if (!globalThis.ReadableStream) {
  globalThis.ReadableStream = ReadableStream;
}

// 2. Ciclo de vida de MSW
beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());