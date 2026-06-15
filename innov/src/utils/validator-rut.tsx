/**
 * Valida un RUT chileno
 */

export const validateRut = (rut: string): boolean => {
  if (!rut || rut.trim().length > 12) return false;

  let cleanRut = rut.replace(/\./g, '').replace(/-/g, '').trim().toUpperCase();

  if (cleanRut.length < 8) return false;

  const body = cleanRut.slice(0, -1);
  const dv = cleanRut.slice(-1);

  if (!/^\d+$/.test(body)) return false;

  let sum = 0;
  let multiplier = 2;

  for (let i = body.length - 1; i >= 0; i--) {
    sum += parseInt(body.charAt(i)) * multiplier;
    multiplier = multiplier === 7 ? 2 : multiplier + 1;
  }

  const expectedDv = 11 - (sum % 11);
  
  let finalDv = '';
  if (expectedDv === 11) finalDv = '0';
  else if (expectedDv === 10) finalDv = 'K';
  else finalDv = expectedDv.toString();

  return finalDv === dv;
};