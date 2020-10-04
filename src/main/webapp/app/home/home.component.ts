import { Component, OnInit, OnChanges, SimpleChanges} from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';

import { Account, LoginModalService, Principal } from '../shared';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
        private router: Router
    ) {
    }

/*
    ngOnChanges(changes: SimpleChanges) {
        if(this.isNotAuthenticated()){
            this.redirectToRegister();
        }
      }
*/
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
            if(!this.isAuthenticated()){
                this.redirectToRegister();
            }
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;

            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    isNotAuthenticated() {
        return !this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
    redirectToRegister(){
        this.router.navigate(['/register']);
    }

    redirectToHome(){
        this.router.navigate(['/']);
    }

}
