import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import swal from "sweetalert2";
import Swal from "sweetalert2";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'app-authorizer-approve-application',
    templateUrl: './authorizer-approve-application.component.html',
    styleUrls: ['./authorizer-approve-application.component.css']
})
export class AuthorizerApproveApplicationComponent implements OnInit {
    params: Params;
    isLoaded: boolean = false;

    constructor(private route: ActivatedRoute, private membershipToTcService: MembershipToTcService) {
    }

    ngOnInit(): void {
        this.route.queryParams.subscribe((params: Params) => {
            this.params = params;
            const id = params['applicationID'];
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            });

            swalWithBootstrapButtons.fire({
                title: 'Are you sure your want to authorize this request?',
                text: 'You won\'t be able to reverse this!',
                icon: 'success',
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                cancelButtonText: 'No!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    if (id) {
                        this.membershipToTcService.approveAppointmentEmailAuthorizer(id).subscribe(
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
                } else if (
                    /* Read more about handling dismissals below */
                    result.dismiss === swal.DismissReason.cancel
                ) {
                    console.log("here")

                    this.membershipToTcService.rejectAppointmentEmailAuthorizer(id).subscribe(
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
                                    text: 'Request Denial Has Been Confirmed.',
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


                    // swalWithBootstrapButtons.fire(
                    //     'Not Authorized',
                    //     'You have not authorized this request',
                    //     'error'
                    // ).then(r =>
                    //     console.log("here")
                    // );


                }
            })


        });

    }

}
