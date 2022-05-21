<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/responsive/quote"%>


<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

</br>
<div class="row">
	<div class="col-xs-12 pull-right cart-actions--print">
		<div class="cart__actions border">
			<div class="row">
				<form:form method="post" modelAttribute="pfeUpdateState"
					action="/my-account/my-quotes/${quoteCode}">
					<c:if test="${quoteData.state.code eq 'SELLERAPPROVER_APPROVED'}">
						<div class="col-sm-4 col-md-3 pull-right">
							<button type="Submit" name="action" value="VALID"
								class="btn btn-primary btn-block btn--validate-quote js-validate-quote-button">
								<spring:theme code="pfe.quote.validate" />
							</button>
						</div>
					</c:if>
					<c:if
						test="${quoteData.state.code eq 'BUYER_APPROVED' or quoteData.state.code eq 'SELLERAPPROVER_APPROVED'or
						 quoteData.state.code eq 'BUYER_ORDERED' or quoteData.state.code eq 'BUYER_SUBMITTED' or quoteData.state.code eq 'SUBMITTED'
						 or quoteData.state.code eq 'CREATED'}">
						<div class="col-sm-4 col-md-3 pull-right">
							<button type="Submit" name="action" value="CANCEL"
								class="btn btn-default btn-block btn--cancel-quote js-cancel-button">
								<spring:theme code="pfe.quote.cancel" />
							</button>
						</div>
					</c:if>
				</form:form>
			</div>
		</div>
	</div>
</div>
<c:if test="${not empty quoteData.entries}">
	<div class="cart-items">
		<quote:quoteItems quoteData="${quoteData}" />
	</div>
</c:if>