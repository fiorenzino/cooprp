export var PERMISSION = {
    'ADMIN': ['Admin'],
    'VIEW': ['Admin', 'Web']
};

export const APP_PROTOCOL = window.location.protocol + '//';
export const APP_HOST = window.location.hostname + ':';
export const APP_PORT = window.location.port;
export const APP_CONTEXT = '/cooprp';
export const APP_API = '/api/v1/';
export const APP_NAME = APP_PROTOCOL + APP_HOST + APP_PORT + APP_CONTEXT + APP_API;


export const COMPANY_CONFIGURATIONS_URL = APP_NAME + 'companyconfigurations';
export const LANGUAGES_URL = APP_NAME + 'languages';
export const LOCATIONS_URL = APP_NAME + 'locations';
export const NOTIFICATIONS_URL = APP_NAME + 'notifications';
export const OPERATIONS_URL = APP_NAME + 'operations';
export const WORKSHIFTS_URL = APP_NAME + 'workshifts';

export const TOKEN_ITEM: string = 'cooprs.auth_token';

export const APP_VERSION: string = '1.0';
