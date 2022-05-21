<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/responsive/quote"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:set var="searchUrl"
	value="/my-account/my-quotes?sort=${ycommerce:encodeUrl(searchPageData.pagination.sort)}" />

<div class="account-section-header">
	<spring:theme code="text.account.quote.myquotes" />
</div>

<c:if test="${empty searchPageData.results}">
	<div class="row">
		<div class="col-md-6 col-md-push-3">
			<div class="account-section-content	content-empty">
				<ycommerce:testId code="quoteHistory_noQuotes_label">
					<spring:theme code="text.account.quotes.noQuotes" />
				</ycommerce:testId>
			</div>
		</div>
	</div>
</c:if>

<c:if test="${not empty searchPageData.results}">
	<div class="account-section-content	">
		<div class="account-orderhistory">
			<div class="account-orderhistory-pagination">
				<nav:pagination top="true" msgKey="text.account.quote.page"
					showCurrentPageInfo="true" hideRefineButton="true"
					supportShowPaged="${isShowPageAllowed}"
					supportShowAll="${isShowAllAllowed}"
					searchPageData="${searchPageData}" searchUrl="${searchUrl}"
					numberPagesShown="${numberPagesShown}" />
			</div>



			<div class="account-overview-table">
				<table class="orderhistory-list-table responsive-table">
					<thead>
						<tr
							class="account-orderhistory-table-head responsive-table-head hidden-xs">
							<th id="header1"><spring:theme
									code='text.account.quote.name' /></th>
							<th id="header2"><spring:theme
									code='text.account.quote.code' /></th>
							<th id="header3"><spring:theme
									code='text.account.quote.date.updated' /></th>
							<th id="header4"><spring:theme
									code='text.account.quote.status' /></th>
							<th id="header5"><spring:theme code='' /></th>

						</tr>
					</thead>
					<tbody>
						<c:forEach items="${searchPageData.results}" var="quote">

							<tr class="responsive-table-item">
								<ycommerce:testId code="orderHistoryItem_orderDetails_link">
									<td headers="header1" class="hidden-sm hidden-md hidden-lg">
										<spring:theme code="text.account.quote.name" />
									</td>
									<td class="responsive-table-cell"><spring:url
											value="/my-account/my-quotes/{/quotecode}/"
											var="quoteDetailLink" htmlEscape="false">
											<spring:param name="quotecode" value="${quote.code}" />
										</spring:url> <a href="${fn:escapeXml(quoteDetailLink)}"
										class="responsive-table-link">${fn:escapeXml(quote.name)}</a>
									</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.quote.code" /></td>
									<td class="responsive-table-cell">
										${fn:escapeXml(quote.code)}</td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.quote.date.updated" /></td>
									<td class="responsive-table-cell"><fmt:formatDate
											value="${quote.updatedTime}" dateStyle="medium"
											timeStyle="short" type="both" /></td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="text.account.quote.status" /></td>
									<td class="status" id="status-${quote.code}"><spring:theme
											code="text.account.quote.status.display.${quote.state}" /> </br>
										<c:if test="${quote.state.code eq 'BUYER_ORDERED'}">
											<spring:theme code="text.account.quote.order.message" />
											<spring:url value="/my-account/order/{/orderCode}"
												var="orderDetailsUrl">
												<spring:param name="orderCode" value="${quote.code}" />
											</spring:url>
											<a href="${fn:escapeXml(orderDetailsUrl)}"
												class="responsive-table-link">${fn:escapeXml(quote.code)}</a>
										</c:if></td>
									<td class="hidden-sm hidden-md hidden-lg"><spring:theme
											code="" /></td>

									<td class="responsive-table-cell">
										<div class="row">
											<form:form method="post" modelAttribute="pfeUpdateState">
												<input type="hidden" name="quoteCode" value="${quote.code}" />
												<c:if
													test="${quote.state.code eq 'SELLERAPPROVER_APPROVED'}">
													<div class="pull-right">
														<button type="Submit" name="action" value="VALID"
															class="btn btn-primary btn-block btn--validate-quote js-validate-quote-button">
															<spring:theme code="pfe.quotes.validate" />
														</button>
													</div>
												</c:if>
												<c:if
													test="${quote.state.code eq 'BUYER_APPROVED' or quote.state.code eq 'SELLERAPPROVER_APPROVED'or
						 								quote.state.code eq 'BUYER_ORDERED' or quote.state.code eq 'BUYER_SUBMITTED' or quote.state.code eq 'SUBMITTED'
														 or quote.state.code eq 'CREATED'}">
													<div class="pull-right">
														<button type="Submit" name="action" value="CANCEL"
															class="btn btn-default btn-block btn--cancel-quote js-cancel-button">
															<spring:theme code="pfe.quotes.cancel" />
														</button>
													</div>
												</c:if>
											</form:form>
										</div>
									</td>
								</ycommerce:testId>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="account-orderhistory-pagination">
			<nav:pagination top="false" msgKey="text.account.quote.page"
				showCurrentPageInfo="true" hideRefineButton="true"
				supportShowPaged="${isShowPageAllowed}"
				supportShowAll="${isShowAllAllowed}"
				searchPageData="${searchPageData}" searchUrl="${searchUrl}"
				numberPagesShown="${numberPagesShown}" />
		</div>
	</div>
</c:if>
