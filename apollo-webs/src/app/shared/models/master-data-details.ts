export class UserDetails {
  id?: number;
  firstName?: string;
  lastName?: string;
  userName?: string;
  email?: string;
  status?: boolean;
}

export class DivisionDetails {
  id?: string;
  division?: string;
  descriptions?: string;
  status?: string;
  departmentId?: number;
}

export class StandardProductCategory {
  id?: number;
  standardCategory?: string;
  standardNickname?: string;
  standardId?: number;
  status?: boolean;
}

export class ProductCategories {
  id?: number;
  name?: string;
  status?: boolean;
  broadProductCategoryId?: number;
}

export class BroadProductCategory {
  id?: number;
  category?: string;
  status?: boolean;
  divisionId?: number;
}

export class Products {
  id?: number;
  name?: string;
  status?: boolean;
  productCategoryId?: number;
}

export class ProductSubcategory {
  id?: number;
  name?: string;
  status?: boolean;
  productId?: number;
}

export class Counties {
  id?: number;
  county?: string;
  regionId?: number;
  status?: boolean;
}

export class Towns {
  id?: number;
  town?: string;
  countyId?: number;
  status?: boolean;
}

export class Department {
  id?: number;
  department?: string;
  descriptions?: string;
  directorateId?: number;
  status?: boolean;
}


export class DesignationEntityDto {
  id?: number;
  designationName?: string;
  descriptions?: string;
  status?: boolean;
  directorateId?: number;
}

export class RegionsEntityDto {
  id?: number;
  region?: string;
  descriptions?: string;
  status?: boolean;
}

export class DirectoratesEntityDto {
  id?: number;
  directorate?: string;
  status?: boolean;
}

export class SectionsEntityDto {
  id?: number;
  section?: string;
  divisionId?: number;
  descriptions?: string;
  status?: boolean;
}

export class SubSectionsL1EntityDto {
  id?: number;
  subSection?: string;
  sectionId?: number;
  status?: boolean;
}

export class SubSectionsL2EntityDto {
  id?: number;
  subSection?: string;
  subSectionL1Id?: number;
  status?: boolean;
}

export class TitlesEntityDto {
  id?: number;
  title?: string;
  remarks?: string;
  status?: boolean;
}

export class RolesEntityDto {
  id?: bigint;
  roleName?: string;
  descriptions?: string;
  status?: boolean;
}

export class AuthoritiesEntityDto {
  id?: bigint;
  name?: string;
  descriptions?: string;
  status?: boolean;
}

export class UserRequestTypesEntityDto {
  id?: bigint;
  userRequest?: string;
  description?: string;
  status?: boolean;
}

export class UserRequestEntityDto {
  id?: bigint;
  requestId?: bigint;
  userId?: bigint;
  userRoleAssigned?: bigint;
  requestStatus?: boolean;
  description?: string;
  status?: boolean;
}

export class UserCompanyDetailsDto{
 name?: string;
 kraPin?: string;
 userId?: bigint;
 registrationNumber?: string;
 postalAddress?: string;
 companyEmail?: string;
 companyTelephone?: string;
 yearlyTurnover?: string;
 businessLines?: string;
 businessNatures?: string;
 buildingName?: string;
 streetName?: string;
 region?: string;
 county?: string;
 town?: string;
}

export class UserCompanyEntityDto{
 name?: string;
 kraPin?: string;
 userId?: bigint;
 profileType?: bigint;
 registrationNumber?: string;
 postalAddress?: string;
 companyEmail?: string;
 companyTelephone?: string;
 yearlyTurnover?: string;
 businessLines?: bigint;
 businessNatures?: bigint;
 buildingName?: string;
 streetName?: string;
 region?: bigint;
 county?: bigint;
 town?: bigint;
}

export class UserRequestDetailEntityDto {
  id?: bigint;
  requestName?: string;
  userName?: string;
  userId?: bigint;
  userRoleAssignedName?: string;
  requestStatus?: boolean;
  description?: string;
  status?: boolean;
}
