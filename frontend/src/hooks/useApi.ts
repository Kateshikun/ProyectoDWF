import { useState, useCallback } from 'react';
import { handleApiError } from '../utils/errors';

export function useApi<T = unknown>() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const execute = useCallback(async <R = T>(
    fn: () => Promise<{ data: R }>,
  ): Promise<R | null> => {
    setLoading(true);
    setError(null);
    try {
      const { data } = await fn();
      return data;
    } catch (err) {
      const msg = handleApiError(err);
      setError(msg);
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  return { loading, error, execute };
}
