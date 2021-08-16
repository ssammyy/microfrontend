import {Component, OnInit} from '@angular/core';
import {LoadingService} from '../../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {UserEntityService} from '../../../core/store';
import {MasterService} from '../../../core/store/data/master/master.service';
import {UserRegister} from '../../../../../../apollo-webs/src/app/shared/models/user';
import {ActivatedRoute} from '@angular/router';
import {TableData} from '../../../md/md-table/md-table.component';
import {FreightStationsDto, RolesEntityDto} from '../../../core/store/data/master/master.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import swal from 'sweetalert2';

@Component({
    selector: 'app-user-management-profile',
    templateUrl: './user-management-profile.component.html',
    styleUrls: ['./user-management-profile.component.css']
})
export class UserManagementProfileComponent implements OnInit {
    currDiv!: string;
    currDivLabel!: string;
    public userID!: string;
    public userDetails!: UserRegister;
    public userRoles!: RolesEntityDto[];
    public userCfs!: FreightStationsDto[];
    public tableData1: TableData;
    public tableData2: TableData;


    public assignRoleForm!: FormGroup;
    public assignCfsForm!: FormGroup;

    rolesEntity: RolesEntityDto[] = [];
    cfsEntity: FreightStationsDto[] = [];

    constructor(
        private route: ActivatedRoute,
        private _loading: LoadingService,
        private SpinnerService: NgxSpinnerService,
        private service: UserEntityService,
        private masterService: MasterService,
        private formBuilder: FormBuilder
    ) {
    }

    ngOnInit(): void {
        this.getSelectedUser();

        this.assignRoleForm = this.formBuilder.group({
            roleID: ['', Validators.required],
            // approvedRemarks: ['', Validators.required],
        });

        this.assignCfsForm = this.formBuilder.group({
            cfsID: ['', Validators.required],
            // approvedRemarks: ['', Validators.required],
        });
    }

    get formAssignRole(): any {
        return this.assignRoleForm.controls;
    }

    get formAssignCfs(): any {
        return this.assignCfsForm.controls;
    }


    private getSelectedUser() {
        this.route.fragment.subscribe(params => {
            this.userID = params;
            console.log(this.userID);
            this.masterService.loadUserDetails(this.userID).subscribe(
                (data: UserRegister) => {
                    this.userDetails = data;
                    console.log(this.userDetails);
                    this.getUserSelectedRoles();
                    if (this.userDetails.employeeProfile != null) {
                        this.getUserSelectedCFS(this.userDetails?.employeeProfile?.profileId);
                    }
                }
            );

        });
    }

    private getUserSelectedRoles() {
        this.masterService.loadUsersAssignedRoles(this.userDetails?.id, 1).subscribe(
            (data1: any) => {
                let formattedArrayAssignedRolesList = [];
                this.userRoles = data1;
                formattedArrayAssignedRolesList = this.userRoles.map(i => [i.roleName, i.descriptions, i.id]);
                this.tableData1 = {
                    headerRow: ['ROLE_NAME', 'DESCRIPTIONS', 'Action'],
                    dataRows: formattedArrayAssignedRolesList
                };
                this.masterService.loadRolesSystemAdmin().subscribe(
                    (data: any) => {
                        this.rolesEntity = data;
                    }
                );

            }
        );
    }

    private getUserSelectedCFS(userProfileId: bigint) {
        this.masterService.loadUsersAssignedCFS(userProfileId, 1).subscribe(
            (data1: any) => {
                let formattedArrayAssignedCfsList = [];
                this.userCfs = data1;
                formattedArrayAssignedCfsList = this.userCfs.map(i => [i.cfsCode, i.cfsName, i.description, i.id]);
                this.tableData2 = {
                    headerRow: ['CFS Code', 'CFS Name', 'Descriptions', 'Action'],
                    dataRows: formattedArrayAssignedCfsList
                };
                this.masterService.loadCfsSystemAdmin().subscribe(
                    (data: any) => {
                        this.cfsEntity = data;
                    }
                );

            }
        );
    }

    openModalAction(viewDiv: string) {
        const arrHead = ['addRole', 'addCfs'];
        const arrHeadSave = ['Assign Role To User', 'Assign CFS To User'];

        for (let h = 0; h < arrHead.length; h++) {
            if (viewDiv === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }

        this.currDiv = viewDiv;
    }

    reloadCurrentRoute() {
        location.reload();
    }

    onSubmitAssignedRole(userId: bigint) {

        this.masterService.assignRoleToUser(userId, this.assignRoleForm.get('roleID')?.value, 1).subscribe(
            (data: any) => {
                this.reloadCurrentRoute();
                swal.fire({
                    title: 'User Assigned Role Successfully',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
            });
    }

    onSubmitAssignedCFS(userProfileId: bigint) {

        this.masterService.assignCfsToUser(userProfileId, this.assignCfsForm.get('cfsID')?.value, 1).subscribe(
            (data: any) => {
                this.reloadCurrentRoute();
                swal.fire({
                    title: 'User Assigned CFS Successfully',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
            });
    }

    revokeRole(roleId: string, userId: bigint) {
        this.masterService.revokeRoleFromUser(userId, roleId, 1).subscribe(
            (data: any) => {
                this.reloadCurrentRoute();
                swal.fire({
                    title: 'Role Revoked Successfully',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
            });
    }

    revokeCfs(cfsId: string, userProfileId: bigint) {
        this.masterService.revokeCfsFromUser(userProfileId, cfsId, 1).subscribe(
            (data: any) => {
                this.reloadCurrentRoute();
                swal.fire({
                    title: 'CFS Revoked Successfully',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
            });
    }
}
