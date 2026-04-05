/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OrderItem } from './OrderItem';
import type { OrgRelationTransfer } from './OrgRelationTransfer';
export type Page_OrgRelationTransfer_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<OrgRelationTransfer>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};

