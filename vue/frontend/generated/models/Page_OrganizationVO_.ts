/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type {OrderItem} from './OrderItem';
import type {OrganizationVO} from './OrganizationVO';

export type Page_OrganizationVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<OrganizationVO>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};

