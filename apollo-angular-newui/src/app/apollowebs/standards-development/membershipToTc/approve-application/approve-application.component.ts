import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import swal from "sweetalert2";

@Component({
    selector: 'app-approve-application',
    templateUrl: './approve-application.component.html',
    styleUrls: ['./approve-application.component.css']
})
export class ApproveApplicationComponent implements OnInit {
    params: Params;
    isLoaded: boolean = false;


    constructor(private route: ActivatedRoute, private membershipToTcService: MembershipToTcService) {
    }


    ngOnInit(): void {

        this.route.queryParams.subscribe((params: Params) => {
            this.params = params;
            const id = params['applicationID'];

            if (id) {
                this.membershipToTcService.approveAppointmentEmail(id).subscribe(
                    (response) => {
                        console.log(response);

                        if (response === 'This Link Has Already Been Used') {
                            swal.fire({
                                title: 'Cancelled',
                                text: 'This Link Has Already Expired.',
                                icon: 'error',
                                customClass: {confirmButton: "btn btn-danger",},
                                buttonsStyling: false
                            }).then((result) => {
                                if (result.value) {
                                    window.location.href = "/login";
                                }
                            });
                        } else if (response === 'Saved') {
                            this.isLoaded = true;
                            swal.fire({
                                title: 'Success',
                                text: 'Your Approval Has Been Confirmed.',
                                icon: 'success',
                                customClass: {confirmButton: "btn btn-success",},
                                buttonsStyling: false
                            }).then((result) => {
                                // if (result.value) {
                                //     window.location.href = "/login";
                                // }
                            });

                        } else {
                            swal.fire({
                                title: 'Cancelled',
                                text: 'This Is Invalid.',
                                icon: 'error',
                                customClass: {confirmButton: "btn btn-danger",},
                                buttonsStyling: false
                            }).then((result) => {
                                if (result.value) {
                                    window.location.href = "/login";
                                }
                            });
                        }
                        // this.notifyService.showSuccess("Success", 'Appointment Letter Sent to ' + reviewApplicationTask.email)

                    },
                    (error: HttpErrorResponse) => {
                        alert(error.message);
                    }
                )


            } else {
                alert("nothing")
            }
        });
    }


}
