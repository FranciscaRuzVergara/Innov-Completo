FROM node:20-alpine

WORKDIR /app

# Copiamos dependencias
COPY package*.json ./

# Instalamos dependencias
RUN npm install

# Copiamos el resto del código
COPY . .

# Exponemos el puerto que usa Vite (por defecto 5173)
EXPOSE 5173

# Comando para arrancar en modo desarrollo
CMD ["npm", "run", "dev", "--", "--host"]