export var PERMISSION = {
    'ADMIN': ['Admin'],
    'VIEW': ['Admin', 'Web']
};

export const APP_PROTOCOL = window.location.protocol + '//';
export const APP_HOST = window.location.hostname + ':';
export const APP_PORT = window.location.port;
export const APP_CONTEXT = '/portale';
export const APP_API = '/api/v1/';
export const APP_NAME = APP_PROTOCOL + APP_HOST + APP_PORT + APP_CONTEXT + APP_API;
export const WS_NAME = 'ws://' + APP_HOST + APP_PORT + APP_CONTEXT;

export const TOKEN_ITEM: string = 'cooprs.auth_token';

export const APP_VERSION: string = '1.0';