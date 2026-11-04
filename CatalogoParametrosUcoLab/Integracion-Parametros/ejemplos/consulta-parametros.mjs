// Ejecutar únicamente en el backend: node consulta-parametros.mjs
// Inyectar estas variables desde el entorno privado del proceso.
const requerido = nombre => {
  const valor = process.env[nombre]?.trim();
  if (!valor) throw new Error(`Falta configurar ${nombre}`);
  return valor;
};

async function consultarMaximoProductos() {
  const base = new URL(requerido('CATALOGO_PARAMETROS_BASE_URL'));
  if (!['http:', 'https:'].includes(base.protocol) || base.username || base.password || base.search || base.hash) {
    throw new Error('URL del catálogo inválida');
  }
  const tokenId = requerido('CATALOGO_PARAMETROS_TOKEN_ID');
  const secreto = requerido('CATALOGO_PARAMETROS_SECRETO');
  const aplicacionId = requerido('CATALOGO_PARAMETROS_APLICACION_ID');
  const ambienteId = requerido('CATALOGO_PARAMETROS_AMBIENTE_ID');
  const parametroId = requerido('CATALOGO_PARAMETROS_PARAMETRO_ID');
  const metadatoId = requerido('CATALOGO_PARAMETROS_METADATO_ID');
  const url = `${base.href.replace(/\/+$/, '')}/tokens/${encodeURIComponent(tokenId)}/parametros`;
  const response = await fetch(url, {
    headers: { Accept: 'application/json', Authorization: `Bearer ${secreto}` },
    signal: AbortSignal.timeout(5000),
    redirect: 'error'
  });
  if (!response.ok) {
    // No incluir el secreto, la URL con IDs ni el cuerpo externo en el error público.
    throw new Error(`La consulta al catálogo falló (HTTP ${response.status})`);
  }
  const datos = await response.json();
  if (datos.aplicacion?.id !== aplicacionId || datos.ambiente?.id !== ambienteId) {
    throw new Error('La respuesta del catálogo no corresponde al alcance configurado');
  }
  if (!Array.isArray(datos.parametros)) throw new Error('Respuesta de parámetros inválida');
  const parametro = datos.parametros.find(p => p.parametro?.id === parametroId);
  const metadatos = parametro?.metadatos;
  const metadato = Array.isArray(metadatos)
    ? metadatos.find(m => m.metadato?.id === metadatoId)?.metadato
    : undefined;
  const maximo = metadato?.valor?.maximoProductos;
  if (metadato?.idAmbiente !== ambienteId || !Number.isSafeInteger(maximo) || maximo <= 0) {
    throw new Error('No hay un máximo de productos válido en el ambiente configurado');
  }
  return maximo;
}

try {
  const maximoProductos = await consultarMaximoProductos();
  // En la aplicación real, aplicar este valor en el caso de uso de Inventario.
  console.log(JSON.stringify({ maximoProductos }));
} catch {
  console.error('No se pudo obtener la configuración de inventario. Revise configuración, alcance, credencial y disponibilidad del catálogo.');
  process.exitCode = 1;
}
