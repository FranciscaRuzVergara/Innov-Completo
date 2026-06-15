/**
 * Valida un correo electrónico restringiendo caracteres especiales
 * y limitando el dominio final a un rango exacto de 2 o 3 caracteres.
 */

export const validateEmail = (email: string): boolean => {
  if (!email) return false;

  const emailRegex = /^[a-zA-Z0-9.\-_]+@[a-zA-Z0-9.\-_]+\.[a-zA-Z]{2,3}$/;

  return emailRegex.test(email);
};