# Consumo de MessageUcoLab con PEP

`CatalogoParametros` es el origen del consumo servidor a servidor y `MessageUcoLab`
es el destino. `BackPrueba` no participa en este flujo.

## Configuración del origen

El starter PEP permanece en la versión `0.1.4`. La identidad técnica se inyecta
por variables de despliegue:

```properties
security.pep.service-identity.enabled=true
security.pep.service-identity.token-uri=${KEYCLOAK_TOKEN_URI}
security.pep.service-identity.client-id=${ORIGIN_TO_DESTINATION_CLIENT_ID}
security.pep.service-identity.client-secret=${ORIGIN_TO_DESTINATION_CLIENT_SECRET}
security.pep.service-identity.timeout=3s
security.pep.service-identity.refresh-skew=30s
```

`MessageUcoLabSecurityConfiguration` agrega el Bearer técnico solamente a las
peticiones cuyo origen coincide con `MESSAGEUCOLAB_URL`. El cliente conserva los
encabezados de aplicación y ambiente que necesita MessageUcoLab.

## Aprovisionamiento en Security

Para el ambiente `dev`, Security debe entregar a CatalogoParametros una identidad
exclusiva y asignarle el rol técnico que permita consultar el recurso de lectura
de mensajes. La autorización debe registrar el método y ruta exactos publicados
por MessageUcoLab, por ejemplo:

```text
GET /messageucolab/v1/public/messages/{codigo}
```

`PEP_REGISTRATION_TOKEN` sigue siendo la credencial de CatalogoParametros como
destino y no se reutiliza como identidad del consumo hacia MessageUcoLab.

## Variables que faltan para activar el flujo

Los valores siguientes no se inventan ni se versionan:

```env
SECURITY_PEP_SERVICE_IDENTITY_ENABLED=true
KEYCLOAK_TOKEN_URI=http://host.docker.internal:19090/realms/security-baseline/protocol/openid-connect/token
ORIGIN_TO_DESTINATION_CLIENT_ID=<client-id-tecnico>
ORIGIN_TO_DESTINATION_CLIENT_SECRET=<client-secret-tecnico>
MESSAGEUCOLAB_URL=<url-publica-de-messageucolab-protegida-por-pep>
```

Mientras Security no entregue esa identidad y el permiso de destino, la
integración permanece desactivada por defecto (`false`) para no arrancar el
backend con secretos inexistentes.
